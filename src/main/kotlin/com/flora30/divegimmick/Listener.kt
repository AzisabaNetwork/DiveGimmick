package com.flora30.divegimmick

import com.flora30.divelib.data.gimmick.Gimmick
import com.flora30.divelib.data.gimmick.GimmickObject
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent

class Listener: Listener {
    val gimmickTick = 10
    val range = 50
    data class horPos(val x: Int,val z: Int)

    @EventHandler
    fun onPlayerMove(e: PlayerMoveEvent){
        // 判定をする回数を減らす
        val player = e.player

        // Layerごとに何のギミックを使うかの設定がある

        //val gimmick: Gimmick = GimmickObject.何もない

        // 水平座標を取得する

        // ギミックに適した場所を探す

        // ギミックのスポーン判定を行う（成功・失敗・失敗記録を残す）
    }

    fun randomHorPos(player: Player){
        // 水平座標をランダムに決める

    }
}