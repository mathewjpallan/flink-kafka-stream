package com.binderror.streaming

import com.binderror.core.KafkaMsg
import org.apache.flink.configuration.Configuration
import org.apache.flink.metrics.Counter
import org.apache.flink.streaming.api.functions.ProcessFunction
import org.apache.flink.util.Collector

class CaseHandlerProcessFunction extends ProcessFunction[KafkaMsg, KafkaMsg]{

  private var eventCounter: Counter = _

  override def processElement(i: KafkaMsg, context: ProcessFunction[KafkaMsg, KafkaMsg]#Context, collector: Collector[KafkaMsg]): Unit = {
      eventCounter.inc()
      i.value.split(" ").map(data => collector.collect(KafkaMsg("", data.toLowerCase())))
  }

  override def open(parameters: Configuration): Unit = {
    super.open(parameters)
    eventCounter = getRuntimeContext().getMetricGroup().counter("events_count")
  }
}
