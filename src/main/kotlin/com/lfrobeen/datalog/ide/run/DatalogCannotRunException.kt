package com.lfrobeen.datalog.ide.run

import com.intellij.execution.ExecutionException


class DatalogCannotRunException(message: String) : ExecutionException(message) {
    companion object {
        fun interpreterNotSetUp(): DatalogCannotRunException {
            return DatalogCannotRunException("Datalog executable is not specified.")
        }

        fun fileNotSetUp(): DatalogCannotRunException {
            return DatalogCannotRunException("File is not specified.")
        }
    }
}
