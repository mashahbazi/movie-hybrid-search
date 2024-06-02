package com.moviehybridsearch.embedding.gateway

import jakarta.annotation.PostConstruct
import org.apache.commons.math3.linear.MatrixUtils
import org.apache.commons.math3.linear.RealMatrix
import org.springframework.ai.embedding.EmbeddingClient
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Primary
import org.springframework.core.io.ResourceLoader
import org.springframework.stereotype.Component
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

@Component
@Primary
class EmbeddingClientDecorator(
    @Qualifier("ollamaEmbeddingClient") private val embeddingClient: EmbeddingClient,
    private val resourceLoader: ResourceLoader,
) : EmbeddingClient by embeddingClient {
    private lateinit var projectionMatrix: RealMatrix

    /**
     * Load the projection matrix from resources
     */
    @PostConstruct
    private fun loadMatrix() {
        val resource = resourceLoader.getResource("classpath:projection_matrix.txt")
        try {
            resource.inputStream.use { inputStream ->
                BufferedReader(InputStreamReader(inputStream)).use { reader ->
                    val data =
                        reader.readLines().map { line ->
                            line.split(" ").map { it.toDouble() }.toDoubleArray()
                        }.toTypedArray()
                    projectionMatrix = MatrixUtils.createRealMatrix(data)
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    /**
     * Reduce the dimensionality of a vector using the projection matrix
     *
     * This will project the vector to a lower dimension based on projection matrix which is from 4096 to 1999
     */
    private fun reduceDim(vector: DoubleArray): DoubleArray {
        val newVectorMatrix = MatrixUtils.createColumnRealMatrix(vector)
        val projectedVector = projectionMatrix.multiply(newVectorMatrix)
        return projectedVector.getColumn(0)
    }

    override fun embed(content: String): List<Double> {
        val vector = embeddingClient.embed(content)
        return reduceDim(vector.toDoubleArray()).map { it / 1000 }.toList()
    }
}
