package com.dam2d.pedrojg2ot

class Champion(champ_key:String, champ_name:String, champ_type:String, champ_image:String, champ_att:String, champ_def:String, champ_mgc:String, champ_diff:String) {
    private var key:String = champ_key
    private var name:String = champ_name
    private var type:String = champ_type
    private var image:String = champ_image
    private var att:String = champ_att
    private var def:String = champ_def
    private var mgc:String = champ_mgc
    private var diff:String = champ_diff
    var mastery:Int = 0
    var mastPoints:Long = 0

    public fun getKey(): String {
        return key
    }

    public fun getName(): String {
        return name
    }

    public fun getType(): String {
        return type
    }

    public fun getImage(): String {
        return image
    }

    public fun getAtt(): String {
        return att
    }

    public fun getDef(): String {
        return def
    }
    public fun getMgc(): String {
        return mgc
    }

    public fun getDiff(): String {
        return diff
    }

}