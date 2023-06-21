package com.flora30.divegimmick

import com.flora30.divelib.DiveLib
import com.flora30.divelib.data.gimmick.Gimmick
import com.flora30.divelib.data.gimmick.GimmickObject
import com.flora30.divelib.data.gimmick.action.*
import com.flora30.divelib.data.gimmick.condition.*
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

object Config {
    private val file: File = File(DiveLib.plugin.dataFolder.absolutePath,File.separator+"gimmick.yml")

    fun load(){
        val config = YamlConfiguration.loadConfiguration(file)

        // ファイル内のkeyを検索
        for (key in config.getKeys(false)){
            val section = config.getConfigurationSection(key)!!

            val spawnRate = section.getInt("SpawnRate",1)
            val group = section.getString("Group",null)
            val random = section.getDouble("Random",1.0)
            val logTime = section.getInt("LogTime",0)

            val conditions = ArrayList<GCondition>()
            for (con in section.getStringList("Conditions")){
                val conData = con.split(" ")
                when(conData[0]){
                    "Apart" -> {
                        conditions.add(GCApart(conData[1].toInt()))
                    }
                    "Movable" -> {
                        conditions.add(GCMovable(conData[1].toBoolean()))
                    }
                    "NearBlock" -> {
                        val materialStrList = conData[3].split(",")
                        val materialList = arrayListOf<Material>()
                        for (m in materialStrList){
                            materialList.add(Material.valueOf(m))
                        }

                        conditions.add(GCNearBlock(Direction.valueOf(conData[1]),conData[2].toInt(),materialList))
                    }
                    "NearPlayer" -> {
                        conditions.add(GCNearPlayer(conData[1].toInt(),conData[2].toBoolean()))
                    }
                    "NearMob" -> {
                        conditions.add(GCNearMob(conData[1].toInt(),conData[2].toBoolean()))
                    }
                    "NearMySpawned" -> {
                        conditions.add(GCNearMySpawned(conData[1].toInt(),conData[2].toBoolean()))
                    }
                    "NearLayerSpawned" -> {
                        conditions.add(GCNearLayerSpawned(conData[1].toInt(),conData[2].toBoolean()))
                    }
                }
            }

            val actions = ArrayList<GAction>()
            for (act in section.getStringList("Actions")){
                val actData = act.split(" ")
                when(actData[0]){
                    "SpawnChest" -> {
                        actions.add(GASpawnChest(ChestType.valueOf(actData[1])))
                    }
                    "SpawnMob" -> {
                        actions.add(GASpawnMob(actData[1]))
                    }
                    "SpawnLayerMob" -> {
                        actions.add(GASpawnLayerMob(SpawnType.valueOf(actData[1])))
                    }
                }
            }

            val gimmick = Gimmick(
                conditions,
                actions,
                spawnRate,
                group,
                random,
                logTime
            )
            GimmickObject.INSTANCE.gimmickMap[key] = gimmick
            Bukkit.getLogger().info("[DiveGimmick]ギミック「$key」を読み込みました")
        }
    }
}