package net.yakclient.event.api.fsm

import kotlinx.coroutines.runBlocking
import net.yakclient.event.api.EventData
import java.time.Instant
import java.util.function.Predicate

public open class TimedEventState(
    override val name: String,
    override val exits: List<Transition>
) : PredicateEventState {
    private lateinit var last: Instant

    override fun accept(): Unit = runBlocking {
        last = Instant.now()
    }

    override fun <T : EventData> accept(event: T): Unit = find(event)?.run {
        this@run.accept(if (this@run is TimedTransition<*>) TimedEventData(event, last) else event)
        last = Instant.now()
    } ?: Unit

    override fun <T : EventData> find(event: T): PredicateTransition? =
        exits.filterIsInstance<TimedTransition<*>>().find {
            it.type.isAssignableFrom(event::class.java)
        } ?: exits.filterIsInstance<TypedPredicateTransition<*>>().find {
            it.type.isAssignableFrom(event::class.java)
        }

    public data class TimedEventData<T : EventData>(
        public val data: T,
        public val instant: Instant
    ) : EventData
}

public class TimedTransition<T : EventData>(
    to: EventState,
    reference: FSMReference,
    public val type: Class<T>,
    predicate: Predicate<TimedEventState.TimedEventData<T>>,
) : PredicateTransition(to, reference, Predicate {
    it is TimedEventState.TimedEventData<*> && type.isAssignableFrom(it.data::class.java) && predicate.test(it as TimedEventState.TimedEventData<T>)
})

