package com.ecommerce.microcommerce.model

import com.fasterxml.jackson.annotation.JsonFilter
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.hibernate.validator.constraints.Length
import org.hibernate.validator.constraints.Range
import org.springframework.data.util.ProxyUtils
import java.io.Serializable
import javax.persistence.*

// https://kotlinexpertise.com/hibernate-with-kotlin-spring-boot/
@MappedSuperclass
abstract class AbstractJpaPersistable<T : Serializable> {

        companion object {
                private val serialVersionUID = -5554308939380869754L
        }

        @Id
        @GeneratedValue(strategy=GenerationType.IDENTITY)
        private var id: T? = null

        fun getId(): T? {
                return id
        }

        override fun equals(other: Any?): Boolean {
                other ?: return false

                if (this === other) return true

                if (javaClass != ProxyUtils.getUserClass(other)) return false

                other as AbstractJpaPersistable<*>

                return if (null == this.getId()) false else this.getId() == other.getId()
        }

        override fun hashCode(): Int {
                return 31
        }

        override fun toString() = "Entity of type ${this.javaClass.name} with id: $id"

}

@Entity
//@JsonFilter("monFiltreDynamique")
class Product (
        @get:Length(min=3, max=20)
        val nom: String,
        @get:Range(min = 0, max=1000)
        val prix: Int,
        val prixAchat: Int
) : AbstractJpaPersistable<Long>()
