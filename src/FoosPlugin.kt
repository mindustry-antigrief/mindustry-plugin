package plugin

import arc.util.*
import arc.util.serialization.*
import mindustry.*
import mindustry.gen.*
import mindustry.mod.*

@Suppress("unused")
class FoosPlugin : Plugin() {
    private val sb = StringBuilder()

    /** Called after command creation */
    override fun init() {
        /** @since v1 Plugin presence check */
        Vars.netServer.addPacketHandler("fooCheck") { player, _ ->
            Call.clientPacketReliable(player.con, "fooCheck", Vars.mods.getMod(this::class.java).meta.version)
            sendCommands(player)
        }

        /** @since v1 Client transmission forwarding */
        Vars.netServer.addPacketHandler("fooTransmission") { player, content ->
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

    /** @since v2 Sends the list of commands to a player */
    private fun sendCommands(player: Player) {
        with(Jval.newObject()) {
            add("prefix", Reflect.get<String>(Vars.netServer.clientCommands.commandList, "prefix"))
            add("commands", Jval.newObject().apply {
                Vars.netServer.clientCommands.commandList.forEach {
                    add(it.text, it.paramText)
                }
            })
            Call.clientPacketReliable(player.con, "commandList", this.toString())
        }
    }
}