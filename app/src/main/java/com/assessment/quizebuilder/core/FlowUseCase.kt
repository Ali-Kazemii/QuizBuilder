package com.assessment.quizebuilder.core

import kotlinx.coroutines.flow.Flow

abstract class FlowUseCase<in Req, Res> where Res : Any? {
    abstract suspend fun run(request: Req): Flow<Response<Res>>
}