package com.ecommerce.microcommerce.dao

import com.ecommerce.microcommerce.model.Product
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*
import javax.transaction.Transactional

@Repository
interface ProductDao : JpaRepository<Product, Long> {

    override fun findById(id: Long): Optional<Product>

    fun findByPrixGreaterThan(prixLimit: Int) : List<Product>

    fun findByNomLike(recherche: String): List<Product>

}
