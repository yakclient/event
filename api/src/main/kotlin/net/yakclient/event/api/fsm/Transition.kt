package net.yakclient.event.api.fsm

import net.yakclient.event.api.EventData
import java.util.function.Consumer

public open class Transition(
    private val to: EventState,
    private val ref: FSMReference,
) : Consumer<EventData> by (Consumer { ref.set(to) })