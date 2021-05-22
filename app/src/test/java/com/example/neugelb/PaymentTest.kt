package com.example.neugelb

import com.example.neugelb.ui.CardInfoFormState
import com.example.neugelb.ui.PaypalInfoFormState
import com.primer.Card
import com.primer.Paypal
import org.junit.Test

import org.junit.Assert.*

class PaymentTest {

    @Test
    fun testToCardPayment() {
        val cardInfoState = CardInfoFormState().apply {
            cardHolderName = "J Doe"
            cardNumber = 4111111111111111
            expirationDate = "03/2030"
            cvv = 737
        }
        val convertedPayment = cardInfoState.toPaymentInstrument()
        assertEquals(convertedPayment.instrument.javaClass, Card::class.java)
        assertEquals((convertedPayment.instrument as Card).cardHolderName, "J Doe")
        assertEquals((convertedPayment.instrument as Card).expirationMonth, "03")
        assertEquals((convertedPayment.instrument as Card).expirationYear, "2030")
        assertEquals((convertedPayment.instrument as Card).cvv, "737")
    }

    @Test
    fun testToPaypalPayment() {
        val paypalInfoFormState = PaypalInfoFormState().apply {
            orderId = 1
        }
        val convertedPayment = paypalInfoFormState.toPaymentInstrument()
        assertEquals(convertedPayment.instrument.javaClass, Paypal::class.java)
        assertEquals((convertedPayment.instrument as Paypal).paypalOrderId, "1")
    }
}