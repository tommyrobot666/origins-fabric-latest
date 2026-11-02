package io.github.apace100.apoli.integration;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import io.github.apace100.apoli.data.container.ContainerType;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

import java.util.Optional;

public final class ContainerTypeCodecEvents {

	/**
	 * 	Called when a {@link ContainerType} is being decoded via the {@link io.github.apace100.apoli.data.ApoliDataTypes#CONTAINER_TYPE} data type.
	 */
	public static final Event<Decoding> DECODING = EventFactory.createArrayBacked(
		Decoding.class,
		callbacks -> new Decoding() {

			@Override
			public <I> Optional<DataResult<Pair<ContainerType, I>>> decode(DynamicOps<I> ops, I input) {

				Optional<DataResult<Pair<ContainerType, I>>> result = Optional.empty();
				for (Decoding callback : callbacks) {

					result = callback.decode(ops, input);

					if (result.isPresent()) {
						break;
					}

				}

				return result;

			}

		}
	);

	/**
	 * 	Called when a {@link ContainerType} is being encoded via the {@link io.github.apace100.apoli.data.ApoliDataTypes#CONTAINER_TYPE} data type.
	 */
	public static final Event<Encoding> ENCODING = EventFactory.createArrayBacked(
		Encoding.class,
		callbacks -> new Encoding() {

			@Override
			public <I> Optional<DataResult<I>> encode(ContainerType input, DynamicOps<I> ops, I prefix) {

				Optional<DataResult<I>> result = Optional.empty();
				for (Encoding callback : callbacks) {

					result = callback.encode(input, ops, prefix);

					if (result.isPresent()) {
						break;
					}

				}

				return result;

			}

		}
	);

	public interface Decoding {
		<I> Optional<DataResult<Pair<ContainerType, I>>> decode(DynamicOps<I> ops, I input);
	}

	public interface Encoding {
		<I> Optional<DataResult<I>> encode(ContainerType input, DynamicOps<I> ops, I prefix);
	}

}
