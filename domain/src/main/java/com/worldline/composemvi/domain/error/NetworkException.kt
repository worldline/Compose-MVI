package com.worldline.composemvi.domain.error

import com.worldline.composemvi.domain.model.StatusCode
import java.io.IOException

class NetworkException(val code: StatusCode) : IOException()
