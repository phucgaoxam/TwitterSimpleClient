package sts.phucchau.twittersimpleclient.model

data class TwitterUser(val userId: Long, val token: String, val secret: String, val name: String, val screenName: String,
                       val email: String, val photoUrl: String, val location: String, val following: Int, val followers: Int)