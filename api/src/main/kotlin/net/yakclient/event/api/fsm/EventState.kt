package net.yakclient.event.api.fsm

import net.yakclient.event.api.EventData

public interface EventState {
    public val name: String

    public fun accept() {}

    public fun <T : EventData> accept(event: T)

    public companion object {
        private var id: Int = 0

        @JvmStatic
        public fun defaultName() : String = "unnamed@${id++}"
    }
}
