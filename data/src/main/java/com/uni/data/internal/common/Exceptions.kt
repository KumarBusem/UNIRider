package com.uni.data.internal.common

import java.io.IOException

class ApiException(message: String) : IOException(message)
class RiderLoginException() : IOException()