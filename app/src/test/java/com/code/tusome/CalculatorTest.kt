package com.code.tusome

import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Test

class CalculatorTest {
    lateinit var calculator: Calculator

    @Before
    fun setUp() {
        calculator = Calculator()
    }

    @After
    fun tearDown() {

    }

    @Test
    fun add() {
        val expected = 5
        val addition = calculator.add(2, 3)
        assertEquals(expected, addition)
    }

    @Test
    fun subtract() {
        val expected = 4
        val subtraction = calculator.subtract(7, 3)
        assertEquals(expected, subtraction)
    }

    @Test
    fun multiply() {
        val expected = 10
        val multiplication = calculator.multiply(2, 5)
        assertEquals(expected, multiplication)
    }

    @Test
    fun divide() {
        val expected = 2
        val multiplication = calculator.divide(10, 5)
        assertEquals(expected, multiplication)
    }

    @Test
    fun modulus() {
        val expected = 1
        val remainder = calculator.modulus(5, 2)
        assertEquals(expected, remainder)
    }
    @Test
    fun additionFails(){
        val expected = 4
        val addition = calculator.add(2,3)
        assertNotEquals(expected,addition)
    }
    @Test
    fun subtractionFails(){
        val expected = 5
        val addition = calculator.subtract(11,5)
        assertNotEquals(expected,addition)
    }
    @Test
    fun multiplicationFails(){
        val expected = 15
        val addition = calculator.multiply(5,4)
        assertNotEquals(expected,addition)
    }
    @Test
    fun divisionFails(){
        val expected = 5
        val addition = calculator.divide(20,5)
        assertNotEquals(expected,addition)
    }
    @Test
    fun modulusFails(){
        val expected = 1
        val addition = calculator.modulus(10,2)
        assertNotEquals(expected,addition)
    }
}