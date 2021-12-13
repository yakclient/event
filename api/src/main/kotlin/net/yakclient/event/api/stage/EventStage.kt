package net.yakclient.event.api.stage

import net.yakclient.event.api.EventData
import java.util.function.Function

public fun interface EventStage : Function<EventData, EventData>

