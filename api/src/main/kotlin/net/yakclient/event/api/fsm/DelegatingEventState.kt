package net.yakclient.event.api.fsm

import net.yakclient.event.api.EventData

public abstract class DelegatingEventState(
    override val name: String
) : EventState {
    override fun <T : EventData> accept(event: T): Unit = find(event)?.accept(event) ?: Unit

    public abstract fun <T : EventData> find(event: T): Transition?
}