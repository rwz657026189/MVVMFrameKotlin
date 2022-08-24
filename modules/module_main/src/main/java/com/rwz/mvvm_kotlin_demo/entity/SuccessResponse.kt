package com.rwz.mvvm_kotlin_demo.entity

import com.rwz.lib_comm.config.CODE_SUCCESS
import com.rwz.lib_comm.entity.response.Response

/**
 * date： 2019/12/1 11:22
 * author： rwz
 * description：一定成功的响应
 **/
class SuccessResponse<T>(
    var List: T
) : Response<T>(data = List, msg = "", code = CODE_SUCCESS)