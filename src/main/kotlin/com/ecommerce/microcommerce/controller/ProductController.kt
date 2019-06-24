package com.ecommerce.microcommerce.controller

import com.ecommerce.microcommerce.exception.ProductNotFoundException
import com.ecommerce.microcommerce.dao.ProductDao
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.converter.json.MappingJacksonValue
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.PathVariable
import com.ecommerce.microcommerce.model.Product
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.PutMapping
import java.util.*
import javax.validation.Valid


@Api(description = "API pour les opérations CRUD sur les produits")
@RestController
class ProductController {

    @Autowired
    private lateinit var productDao: ProductDao

    @PostMapping(value = ["Produits"])
    fun addProduct(@Valid @RequestBody product: Product): ResponseEntity<Unit> {
        val addedProduct = productDao.save(product) //?: return ResponseEntity.noContent().build<Unit>()
        val location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(addedProduct.getId())
                .toUri()
        return ResponseEntity.created(location).build()
    }

    @GetMapping(value = ["Produits"])
    fun listerProduits(): MappingJacksonValue {
        val products = productDao.findAll()
        val monFiltre = SimpleBeanPropertyFilter.serializeAllExcept("prixAchat")
        val listeDeNosFiltres = SimpleFilterProvider().addFilter("monFiltreDynamique", monFiltre)
        val produitsFiltre = MappingJacksonValue(products)
        produitsFiltre.filters = listeDeNosFiltres
        return produitsFiltre
    }

    @ApiOperation(value = "Récupère un produit grâce à son ID à condition que celui-ci soit en stock!")
    @GetMapping(value = ["Produits/{id}"])
    fun afficherUnProduit(@PathVariable id: Long): Product? {

        val product: Optional<Product> = productDao.findById(id)
        if(!product.isPresent) throw ProductNotFoundException("Le produit avec l'id $id est INTROUVABLE.")

        return productDao.findById(id).orElseGet(null)
    }

    @GetMapping(value = ["test/produits/prix/{prixLimit}"])
    fun testeDeRequetes(@PathVariable prixLimit: Int): List<Product> {
        return productDao.findByPrixGreaterThan(400)
    }

    @GetMapping(value = ["test/produits/nom/{recherche}"])
    fun testeDeRequetes2(@PathVariable recherche: String): List<Product> {
        return productDao.findByNomLike("%$recherche%")
    }

    @DeleteMapping(value = ["/Produits/{id}"])
    fun supprimerProduit(@PathVariable id: Long): ResponseEntity<Unit> {

        val productDb : Optional<Product> = productDao.findById(id)
        if (productDb.isPresent) {
            productDao.deleteById(id)
            return ResponseEntity.accepted().build()
        } else {
            throw ProductNotFoundException("Le produit $id est INTROUVABLE.")
        }
    }

    @PutMapping(value = ["/Produits"])
    fun updateProduit(@RequestBody product: Product): ResponseEntity<Unit> {

        val id = product.getId()
        // code à mettre dans un service
        val productDb : Optional<Product> = if (id != null) productDao.findById(id) else Optional.empty()
        if (productDb.isPresent) {
            productDao.save(product)
            return ResponseEntity.accepted().build()
        } else {
            if (id == null){
                return ResponseEntity.badRequest().build()
            } else {
                throw ProductNotFoundException("Le produit $id est INTROUVABLE.")
            }
        }
    }
}