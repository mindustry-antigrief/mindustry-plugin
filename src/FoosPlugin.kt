package foo

import arc.util.*
import arc.util.serialization.*
import mindustry.*
import mindustry.gen.*
import mindustry.mod.*
import mindustry.net.*
import mindustry.net.Administration.*
import java.io.*

@Suppress("unused")
class FoosPlugin : Plugin() {
    private val sb = StringBuilder()
    private val version by lazy { Vars.mods.getMod(this::class.java).meta.version }
    private val transmissions = Config("fooForwardTransmissions", "Whether client transmissions (chat, dms, and more) are relayed through the server", true) { enableTransmissions() }
    private val commands = Config("fooCommandList", "Whether Foo's users are sent the command list on join (for autocomplete)", true)

    /** Called after command creation */
    override fun init() {
        Log.info("Initialized Foo's Plugin v$version")

        /** @since v1 Plugin presence check */
        Vars.netServer.addPacketHandler("fooCheck") { player, _ ->
            Call.clientPacketReliable(player.con, "fooCheck", version)
            enableTransmissions(player)
            sendCommands(player)
        }

        /** @since v1 Client transmission forwarding */
        Vars.netServer.addPacketHandler("fooTransmission") { player, content ->
            if (!transmissions.bool()) return@addPacketHandler
            sb.append(player.id).append(" ").append(content)
            Call.clientPacketReliable("fooTransmission", sb.toString())
            sb.setLength(0) // Reset the builder
        }
    }

    /** Console commands */
    override fun registerServerCommands(handler: CommandHandler) {

    }

    /** In game commands */
    override fun registerClientCommands(handler: CommandHandler) {

    }

    /** @since v2 Informs clients of the transmission forwarding state. When [player] is null, the status is sent to everyone */
    private fun enableTransmissions(player: Player? = null) {
        val enabled = transmissions.bool().toString()
        if (player != null) Call.clientPacketReliable(player.con, "fooTransmissionEnabled", enabled)
        else Call.clientPacketReliable("fooTransmissionEnabled", enabled)
    }

    /** @since v2 Sends the list of commands to a player */
    private fun sendCommands(player: Player) {
        if (!commands.bool()) return
        with(Jval.newObject()) {
            add("prefix", Reflect.get<String>(Vars.netServer.clientCommands, "prefix"))
            add("commands", Jval.newObject().apply {
                Vars.netServer.clientCommands.commandList.forEach {
                    add(it.text, it.paramText)
                }
            })
            Call.clientPacketReliable(player.con, "commandList", this.toString())
        }
    }
}