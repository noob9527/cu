package cn.staynoob.cu.domain

import java.io.Serializable

interface IdReference<out T : Serializable> {
    val id: T
}

