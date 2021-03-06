package net.yakclient.event.api.fsm

import net.yakclient.event.api.EventData
import java.util.function.Predicate

public open class TypedPredicateEventState(
    name: String? = null,
    override val exits: List<Transition>,
) : PredicateEventState {
    public override val name: String = name ?: "unnamed@${System.identityHashCode(this)}"

    override fun <T : EventData> find(event: T): Transition? =
        exits.filterIsInstance<TypedPredicateTransition<*>>().find { it.type.isAssignableFrom(event::class.java) } ?: exits.firstOrNull()
}

public class TypedPredicateTransition<T : EventData>(
    to: EventState, ref: FSMReference, public val type: Class<T>,

    predicate: Predicate<T>
) : PredicateTransition(to, ref, predicate as Predicate<EventData>) {
    override fun accept(t: EventData) {
        if (type.isAssignableFrom(t::class.java)) super.accept(t)
    }
}