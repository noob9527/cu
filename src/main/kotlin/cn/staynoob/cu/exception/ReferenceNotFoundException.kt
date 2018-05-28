package cn.staynoob.cu.exception

import cn.staynoob.cu.domain.Reference

class ReferenceNotFoundException(
        reference: Reference
) : Exception("reference: $reference does't exist") {
    override val message: String
        get() = super.message!!
}