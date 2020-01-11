package net.frozendevelopment.bridgeio.services.discovery

enum class DiscoveryEvent {
    STARTED_DISCOVERY,
    STOPPED_DISCOVERY,
    DISCOVERY_FAILED_TO_START,
    DISCOVERY_FAILED_TO_END,
    SERVICE_RESOLVE_FAILED,
    SERVICE_RESOLVE_SUCCESS,
    SERVICE_FOUND,
    SERVICE_LOST,
}