@file:Suppress("ktlint:standard:filename")

package com.moviehybridsearch.shared.extension

import org.slf4j.Logger
import org.slf4j.LoggerFactory

var Any.logger: Logger
    get() = LoggerFactory.getLogger(this.javaClass)
    private set(_) {}
