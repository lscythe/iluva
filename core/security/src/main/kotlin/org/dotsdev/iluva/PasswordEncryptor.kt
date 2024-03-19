package org.dotsdev.iluva

import at.favre.lib.crypto.bcrypt.BCrypt
import java.security.SecureRandom

interface IPasswordEncryptor {
    fun validate(input: String, expected: String): Boolean
    fun encrypt(password: String): String
}

object PasswordEncryptor : IPasswordEncryptor {
    private const val LETTERS: String = "abcdefghijklmnopqrstuvwxyz"
    private const val UPPERCASE_LETTERS: String = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    private const val NUMBERS: String = "0123456789"
    private const val SPECIAL: String = "@#=+!£$%&?"
    private const val ALGORITHM = "SHA1PRNG"

    fun generatePassword(
        isWithLetter: Boolean = true,
        isWithUppercase: Boolean = true,
        isWithNumbers: Boolean = true,
        isWithSpecial: Boolean = true,
        length: Int = 8
    ): String {
        var result = ""
        var i = 0

        if (isWithLetter) result += LETTERS
        if (isWithUppercase) result += UPPERCASE_LETTERS
        if (isWithNumbers) result += NUMBERS
        if (isWithSpecial) result += SPECIAL

        val random = SecureRandom.getInstance(ALGORITHM)
        val stringBuilder = StringBuilder(length)

        do {
            val randomInt = random.nextInt(result.length)
            stringBuilder.append(result[randomInt])
            i++
        } while (i < length)

        return stringBuilder.toString()
    }

    override fun validate(input: String, expected: String): Boolean =
        BCrypt.verifyer().verifyStrict(input.toCharArray(), expected.toCharArray()).verified

    override fun encrypt(password: String): String = BCrypt.withDefaults().hashToString(11, password.toCharArray())

}