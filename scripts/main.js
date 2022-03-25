Events.on(EventType.ServerLoadEvent, _ => {
    Vars.netServer.addPacketHandler("fooCheck", (player, _) => // Plugin presence check
        Call.clientPacketReliable(player.con, "fooCheck", Vars.mods.getMod("fooplugin").meta.version)
    )

    Vars.netServer.addPacketHandler("fooTransmission", (player, data) => // Client transmission forwarding
        Call.clientPacketReliable("fooTransmission", player.id + " " + data)
    )
})