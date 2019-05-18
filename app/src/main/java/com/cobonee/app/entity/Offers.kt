package com.cobonee.app.entity

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

data class OffersResponse(
    @field:Json(name = "data")
    val offers: List<Offer>,
    @field:Json(name = "links")
    val links: Links,
    @field:Json(name = "meta")
    val meta: Meta
)

data class Links(
    @field:Json(name = "first")
    val first: String?,
    @field:Json(name = "last")
    val last: String?,
    @field:Json(name = "prev")
    val prev: Any?,
    @field:Json(name = "next")
    val next: Any?
)

data class Meta(
    @field:Json(name = "current_page")
    val currentPage: Int?,
    @field:Json(name = "from")
    val from: Int?,
    @field:Json(name = "last_page")
    val lastPage: Int?,
    @field:Json(name = "path")
    val path: String?,
    @field:Json(name = "per_page")
    val perPage: Int?,
    @field:Json(name = "to")
    val to: Int?,
    @field:Json(name = "total")
    val total: Int?
)

data class Offer(
    @field:Json(name = "id")
    val id: Int?,
    @field:Json(name = "description")
    val offerHeader: String?,
    @field:Json(name = "details")
    val offerBody: String?,
    @field:Json(name = "price")
    val price: String?,
    @field:Json(name = "features")
    val features: String?,
    @field:Json(name = "is_favourite")
    var isFav: Boolean = false,
    @field:Json(name = "name")
    val ownerName: String?,
    @field:Json(name = "owner_phone")
    val ownerPhone: String?,
    @field:Json(name = "address")
    val address: String?,
    @field:Json(name = "cityName")
    val city: OfferCity?,
    @field:Json(name = "facebook")
    val facebook: String?,
    @field:Json(name = "twitter")
    val twitter: String?,
    @field:Json(name = "instagram")
    val instagram: String?,
    @field:Json(name = "discount")
    val discount: String?,
    @field:Json(name = "price_after_discount")
    val priceAfterDiscount: Float?,
    @field:Json(name = "coubons")
    val coubones: List<Coubone?>?,
    @field:Json(name = "photos")
    val photos: List<OfferPhoto?>?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readByte() != 0.toByte(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readParcelable(OfferCity::class.java.classLoader),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Float::class.java.classLoader) as? Float,
        parcel.createTypedArrayList(Coubone),
        parcel.createTypedArrayList(OfferPhoto)
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(offerHeader)
        parcel.writeString(offerBody)
        parcel.writeString(price)
        parcel.writeString(features)
        parcel.writeByte(if (isFav) 1 else 0)
        parcel.writeString(ownerName)
        parcel.writeString(ownerPhone)
        parcel.writeString(address)
        parcel.writeParcelable(city, flags)
        parcel.writeString(facebook)
        parcel.writeString(twitter)
        parcel.writeString(instagram)
        parcel.writeString(discount)
        parcel.writeValue(priceAfterDiscount)
        parcel.writeTypedList(coubones)
        parcel.writeTypedList(photos)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Offer> {
        override fun createFromParcel(parcel: Parcel): Offer {
            return Offer(parcel)
        }

        override fun newArray(size: Int): Array<Offer?> {
            return arrayOfNulls(size)
        }
    }
}

data class Coubone(
    @field:Json(name = "id")
    val id: Int?,
    @field:Json(name = "description")
    val offerHeader: String?,
    @field:Json(name = "price_after_discount")
    val priceAfterDiscount: Float?,
    var quantity: Int = 1
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readValue(Float::class.java.classLoader) as? Float,
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(offerHeader)
        parcel.writeValue(priceAfterDiscount)
        parcel.writeInt(quantity)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Coubone> {
        override fun createFromParcel(parcel: Parcel): Coubone {
            return Coubone(parcel)
        }

        override fun newArray(size: Int): Array<Coubone?> {
            return arrayOfNulls(size)
        }
    }

}

data class OfferPhoto(
    @Json(name = "thumb")
    val thumb: String?,
    @Json(name = "small")
    val small: String?,
    @Json(name = "medium")
    val medium: String?,
    @Json(name = "large")
    val large: String?,
    @Json(name = "original")
    val original: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(thumb)
        parcel.writeString(small)
        parcel.writeString(medium)
        parcel.writeString(large)
        parcel.writeString(original)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<OfferPhoto> {
        override fun createFromParcel(parcel: Parcel): OfferPhoto {
            return OfferPhoto(parcel)
        }

        override fun newArray(size: Int): Array<OfferPhoto?> {
            return arrayOfNulls(size)
        }
    }
}

data class OfferCity(
    @field:Json(name = "id")
    val id: Int,
    @field:Json(name = "name")
    val name: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<OfferCity> {
        override fun createFromParcel(parcel: Parcel): OfferCity {
            return OfferCity(parcel)
        }

        override fun newArray(size: Int): Array<OfferCity?> {
            return arrayOfNulls(size)
        }
    }
}

data class MakeFavouritesResponse(
    @field:Json(name = "message")
    val message: String
)

@Entity(tableName = "cartItem")
data class CartItem(
    @PrimaryKey var itemId: Int,
    var userId: Int,
    var itemQuentity: Int
)