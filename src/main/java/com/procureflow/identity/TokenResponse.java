package com.procureflow.identity;

public record TokenResponse(String accessToken, String tokenType, long expiresInSeconds) { }
