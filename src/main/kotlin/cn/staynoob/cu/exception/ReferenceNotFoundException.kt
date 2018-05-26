package cn.staynoob.cu.exception

import cn.staynoob.cu.domain.Reference

class ReferenceNotFoundException(
        reference: Reference
) : RuntimeException("reference: $reference does't exist")