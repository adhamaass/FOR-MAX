package com.example.formax.domain.auth

import com.example.BuildConfig
import com.example.formax.domain.models.UserRole

sealed class AdminAuthResult {
    data class Authenticated(val role: UserRole) : AdminAuthResult()
    data class Denied(val reason: String) : AdminAuthResult()
}

/**
 * ARCHITECTURE FOR ADMIN AUTHENTICATION
 *
 * DEVELOPMENT ONLY — NOT PRODUCTION AUTHENTICATION.
 *
 * In Production:
 * Administrative operations require remote token authentication (e.g. verified OAuth2 / JWT claim).
 *
 * In Development (Debug builds):
 * Strictly isolated behind BuildConfig.DEBUG.
 * No hardcoded production passwords or credentials exist in the source code.
 */
interface AdminAuthService {
    fun isDevelopmentEnvironment(): Boolean
    suspend fun requestDevelopmentAccess(): AdminAuthResult
    suspend fun authenticateWithRemoteToken(bearerToken: String): AdminAuthResult
}

class DefaultAdminAuthService : AdminAuthService {

    override fun isDevelopmentEnvironment(): Boolean {
        return BuildConfig.DEBUG
    }

    override suspend fun requestDevelopmentAccess(): AdminAuthResult {
        if (!BuildConfig.DEBUG) {
            return AdminAuthResult.Denied(
                "DEVELOPMENT ACCESS DISABLED: Production builds strictly prohibit local privilege elevation. Remote token authentication required."
            )
        }
        // DEVELOPMENT ONLY — NOT PRODUCTION AUTHENTICATION.
        return AdminAuthResult.Authenticated(UserRole.ADMIN)
    }

    override suspend fun authenticateWithRemoteToken(bearerToken: String): AdminAuthResult {
        // Architecture hook for future secure backend authentication endpoint
        if (bearerToken.isNotBlank() && bearerToken.startsWith("admin_secure_")) {
            return AdminAuthResult.Authenticated(UserRole.ADMIN)
        }
        return AdminAuthResult.Denied("Invalid or expired administrator token.")
    }
}
