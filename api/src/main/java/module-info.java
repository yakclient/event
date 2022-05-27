import net.yakclient.event.api.EventDispatcher;

module yakclient.event.api {
    requires kotlin.stdlib;

    requires java.logging;
    requires kotlinx.coroutines.core.jvm;
    requires yakclient.common.util;

    exports net.yakclient.event.api;
    exports net.yakclient.event.api.fsm;
    exports net.yakclient.event.api.stage;

    uses EventDispatcher;
}