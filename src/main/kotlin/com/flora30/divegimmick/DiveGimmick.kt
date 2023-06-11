package com.flora30.divegimmick

import com.flora30.divelib.DiveLib
import com.flora30.divelib.data.gimmick.GimmickObject
import org.bukkit.plugin.java.JavaPlugin

class DiveGimmick : JavaPlugin() {
    override fun onEnable() {
        server.pluginManager.registerEvents(Listener(),this)
        server.getPluginCommand("gimmick")!!.setExecutor(Listener())
        server.scheduler.scheduleSyncRepeatingTask(this, { removeOldLogs() },20,20 )

        Config.load()
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }

    // 時間が経過したログを削除する
    fun removeOldLogs(){
        for (logs in GimmickObject.INSTANCE.layerLogMap.values){
            // ログが出来てからの経過時間 > ギミック固有の削除までの時間
            logs.removeIf { DiveLib.plugin.lagTime - it.time > GimmickObject.INSTANCE.gimmickMap[it.gimmickID]!!.logTime * 1000 }
        }
    }
}