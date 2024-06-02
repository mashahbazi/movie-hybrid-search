package com.moviehybridsearch.vector.usecase

import jakarta.annotation.PostConstruct
import org.apache.commons.math3.linear.MatrixUtils
import org.apache.commons.math3.linear.RealMatrix
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Component
import org.springframework.util.ResourceUtils

@Component
@Lazy
class PCAUseCase {
    private lateinit var projectionMatrix: RealMatrix

    /**
     * Reduce the dimensionality of a vector using the projection matrix
     *
     * This will project the vector to a lower dimension based on projection matrix which is from 4096 to 1999
     */
    fun execute(vector: DoubleArray): DoubleArray {
        val newVectorMatrix = MatrixUtils.createColumnRealMatrix(vector)
        val projectedVector = projectionMatrix.multiply(newVectorMatrix)
        return projectedVector.getColumn(0)
    }

    /**
     * Load the projection matrix from resources
     */
    @PostConstruct
    fun loadMatrix() {
        val file = ResourceUtils.getFile("classpath:projection_matrix.txt")
        val data =
            file.readLines().map { line ->
                line.split(" ").map { it.toDouble() }.toDoubleArray()
            }.toTypedArray()
        projectionMatrix = MatrixUtils.createRealMatrix(data)
    }
}
