package com.flora30.divegimmick

import com.flora30.divelib.DiveLib
import com.flora30.divelib.data.GData
import com.flora30.divelib.data.LayerObject
import com.flora30.divelib.data.gimmick.Gimmick
import com.flora30.divelib.data.gimmick.GimmickLog
import com.flora30.divelib.data.gimmick.GimmickObject
import com.flora30.divelib.data.player.PlayerDataObject
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent

class Listener: Listener {
    val gimmickTick = 10
    val range = 40
    data class HorPos(val x: Int,val z: Int)

    @EventHandler
    fun onPlayerMove(e: PlayerMoveEvent){
        // 判定をする回数を減らす
        if (DiveLib.plugin.tickCount % gimmickTick == 0) return

        // Layerごとに何のギミックを使うかの設定がある
        val player = e.player
        val layerName = LayerObject.INSTANCE.getLayerName(player.location)
        val layer = LayerObject.INSTANCE.layerMap[layerName] ?: return
        // ギミックID
        val gimIDList = layer.gimmickList

        // ギミックIDごとに処理をする
        for (gimID in gimIDList){
            // ギミックを取得
            val gim = GimmickObject.INSTANCE.gimmickMap[gimID] ?: continue

            // spawnRate = 処理をする回数
            for (i in 0..gim.spawnRate){
                // 水平座標を取得する
                val horPos = randomHorPos(player)
                val gData = GData(player, Location(player.world, horPos.x.toDouble(),30.0, horPos.z.toDouble()))
                // ギミックに適した場所を探す
                for(y in 30..230){
                    gData.location.y = y.toDouble()
                    // ギミックに適して居ない場合は次へ
                    if (!(checkAllConditions(gData,gim))) continue

                    // ランダムなスポーン判定を行う
                    if (Math.random() < gim.random){
                        // 成功した場合はActionをする
                        for (action in gim.actions){
                            action.execute(gData)
                        }
                    }

                    // ログを記録する（ランダム判定は失敗しても記録）
                    GimmickObject.INSTANCE.layerLogMap[layerName]?.add(GimmickLog(gData.location,gimID, System.currentTimeMillis()))
                    PlayerDataObject.INSTANCE.playerDataMap[player.uniqueId]?.layerData?.gimmickLogs?.add(GimmickLog(gData.location,gimID, System.currentTimeMillis()))
                    break
                }
            }
        }
    }

    /**
     * 全ての条件がギミックに適しているか判定する
     */
    fun checkAllConditions(gData: GData, gim: Gimmick): Boolean{
        for (con in gim.conditions){
            // 条件に合うか判定する
            if (!(con.check(gData))){
                return false
            }
        }
        return true
    }

    /**
     * プレイヤーの周囲でランダムな水平座標を作る
     */
    fun randomHorPos(player: Player): HorPos {
        val location = player.location

        // 水平座標の差分をランダムに決める
        val x = (-range..range).random()
        val z = (-range..range).random()

        return HorPos(location.blockX + x, location.blockZ + z)
    }
}