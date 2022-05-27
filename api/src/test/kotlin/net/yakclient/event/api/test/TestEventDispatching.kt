package net.yakclient.event.api.test

import net.yakclient.event.api.EventDispatchManager
import net.yakclient.event.api.EventDispatcher
import kotlin.test.Test

class TestEventDispatching {
    @Test
    fun `Test dispatcher loading`() {
        EventDispatchManager.add(TestEventOneDispatcher())
        EventDispatchManager.load(SuperDispatcher::class.java)
    }
}