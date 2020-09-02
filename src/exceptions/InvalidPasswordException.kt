package com.tribuna.exceptions

import java.lang.RuntimeException

class InvalidPasswordException(message: String) : RuntimeException(message)