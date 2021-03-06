package net.yakclient.event.api.fsm

import net.yakclient.event.api.EventData
import java.util.function.Consumer

public class EventTransition(to: EventState, ref: FSMReference, private val event: Consumer<EventData>) : Transition(to, ref) {
    override fun accept(t: EventData) {
        event.accept(t)
        super.accept(t)
    }
}

public fun MutableEventFSM.eventTransition(to: EventState, consumer: Consumer<EventData>): EventTransition =
    EventTransition(to, ref, consumer)

