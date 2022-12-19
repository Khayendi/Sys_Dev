package com.code.tusome

class Calculator {
    fun add(a:Int,b:Int):Int{
        return a+b
    }
    fun subtract(a:Int,b:Int):Int{
        val diff =  if(a>b){
            a-b
        }else{
            b-a
        }
        return diff
    }
    fun multiply(a:Int,b:Int):Int{
        return a*b
    }
    fun divide(a:Int,b:Int): Number {
        val quotient = if (a>b){
            a/b
        }else if (b>a){
            b/a
        }else{
            0.0
        }
        return quotient
    }
    fun modulus(a:Int,b:Int):Number{
        val result = if (a>b){
            a%b
        }else{
            b%a
        }
        return result
    }
}