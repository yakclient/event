package net.yakclient.event.api

import net.yakclient.common.util.ServiceListCollector

public object EventDispatchManager : ServiceListCollector<EventDispatcher<*>>() {
    private fun <T: EventData> find(type: Class<out EventDispatcher<T>>) : EventDispatcher<*>? = services.find {
        type.isAssignableFrom(it::class.java)
    }

    public fun <T : EventData> load(type: Class<out EventDispatcher<T>>): EventDispatcher<T> =
        (find(type) as? EventDispatcher<T>)
            ?: throw IllegalArgumentException("Type: ${type.name} is not a registered event dispatcher!")

    public fun <T : EventData> typeOf(type: Class<out EventDispatcher<T>>): Class<T> = load(type).eventType

    public fun <T : EventData> findSubscriber(type: Class<T>): Class<out EventDispatcher<T>>? =
        services.find { type.isAssignableFrom(it.eventType) } as? Class<out EventDispatcher<T>>?
}