package com.tribuna.exceptions

import java.lang.RuntimeException

class AlreadyExistException(message:String): RuntimeException(message)