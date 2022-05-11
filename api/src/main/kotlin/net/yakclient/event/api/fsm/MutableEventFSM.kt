package net.yakclient.event.api.fsm

import net.yakclient.event.api.EventData
import java.util.function.Predicate

public class MutableEventFSM(
    debug: Boolean = false
) : EventFSM(StatePlaceholder(), debug) {
    public val ref: FSMReference = FSMReference(this)

    public fun of(name: String = EventState.defaultName()): MutableEventState =
        of(TypedPredicateEventState(name, ArrayList()))

    public fun timedOf(name: String = EventState.defaultName()): MutableEventState =
        of(TimedEventState(name, ArrayList()))

    public fun timingOutOf(to: EventState, timeOut: Long, name: String = EventState.defaultName()): MutableEventState =
        of(TimingOutEventState(name, arrayListOf(TimingOutTransition(to, ref)), timeOut))

    public fun of(delegate: PredicateEventState): MutableEventState =
        MutableEventState(delegate).also { if (current is StatePlaceholder) current = it }

    public fun <T : EventState> of(delegate: T): T = delegate.also { if (current is StatePlaceholder) current = it }

    public fun transition(to: EventState): TransitionProvider = TransitionProvider(to)

    public inner class MutableEventState(
        private val delegate: PredicateEventState = TypedPredicateEventState(EventState.defaultName(), ArrayList()),
    ) : EventState by delegate, MutableList<Transition> by delegate.exits as? MutableList<Transition>
        ?: throw IllegalStateException("Event state must be mutable!") {

        public fun transitionsTo(state: EventState): TransitionProvider = FromTransitionProvider(state)

        private inner class FromTransitionProvider(
            to: EventState
        ) : TransitionProvider(to) {
            override fun with(): Transition =
                super.with().also { add(it) }


            override fun <T : EventData> with(
                type: Class<T>,
                predicate: Predicate<T>
            ): Transition =
                super.with(type, predicate).also { add(it) }

            override fun <T : EventData> withTime(
                type: Class<T>,
                predicate: Predicate<TimedEventState.TimedEventData<T>>
            ): TimedTransition<T> =
                super.withTime(type, predicate).also { add(it) }
        }
    }

    public open inner class TransitionProvider internal constructor(
        private val to: EventState
    ) {
        public open fun with(): Transition = Transition(to, ref)

        public open fun <T : EventData> with(
            type: Class<T>,
            predicate: Predicate<T>
        ): Transition = TypedPredicateTransition(to, ref, type, predicate)

        public open fun <T : EventData> withTime(
            type: Class<T>,
            predicate: Predicate<TimedEventState.TimedEventData<T>>
        ): TimedTransition<T> = TimedTransition(to, ref, type, predicate)
    }

    private class StatePlaceholder : EventState {
        override val name: String = "placeholder"

        override fun <T : EventData> accept(event: T): Nothing =
            throw UnsupportedOperationException("The placeholder should not receive events")
    }
}

public inline fun <reified T : EventData> MutableEventFSM.TransitionProvider.with(p: Predicate<T>): Transition =
    with(T::class.java, p)

public inline fun <reified T : EventData> MutableEventFSM.TransitionProvider.withTime(p: Predicate<TimedEventState.TimedEventData<T>>): TimedTransition<T> =
    withTime(T::class.java, p)

public infix fun MutableEventFSM.MutableEventState.transitionsTo(state: EventState): MutableEventFSM.TransitionProvider =
    transitionsTo(state)
