package net.frozendevelopment.bridgeio.services

class InvalidBridgeHostException : Exception("Host on BridgeContext is invalid.")

class InvalidBridgeTokenException : Exception("Token on BridgeContext is invalid.")

class ServiceAlreadyRunningException : Exception("This service is already running and can not be started again.")
