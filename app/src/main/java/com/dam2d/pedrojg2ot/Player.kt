package com.dam2d.pedrojg2ot

class Player {
    var top3champs:ArrayList<Champion> = arrayListOf<Champion>()
    // -------------------------------------------- \\
    var id:String = ""
    var accountId:String = ""
    var puuid:String = ""
    var name:String = ""
    var profileIconId:Int = 0
    var revisionDate:Long = 0
    var summonerLevel:Int = 0
    // -------------------------------------------- \\
    var solo_tier:String = ""
    var solo_rank:String = ""
    var solo_lp:Int = 0
    var solo_wins:Int = 0
    var solo_losses:Int = 0

    var flex_tier:String = ""
    var flex_rank:String = ""
    var flex_lp:Int = 0
    var flex_wins:Int = 0
    var flex_losses:Int = 0


}