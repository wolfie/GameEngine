package com.github.wolfie.engine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

public class MultiHashMap<K, V> {

	public class MultiHashMapEntry implements Entry<K, V> {

		private final K k;
		private V v;

		public MultiHashMapEntry(final K k, final V v) {
			this.k = k;
			this.v = v;
		}

		@Override
		public K getKey() {
			return k;
		}

		@Override
		public V getValue() {
			return v;
		}

		@Override
		public V setValue(final V value) {
			if (containsEntry(k, v)) {
				final V old = v;
				final HashSet<V> values = map.get(k);
				final boolean success = values.remove(old);
				if (success) {
					values.add(value);
					this.v = value;
					return old;
				} else {
					throw new ConcurrentModificationException();
				}
			} else {
				throw new ConcurrentModificationException();
			}
		}
	}

	HashMap<K, HashSet<V>> map = new HashMap<>();

	public int size() {
		int sum = 0;
		for (final HashSet<V> value : map.values()) {
			sum += value.size();
		}
		return sum;
	}

	public boolean isEmpty() {
		return map.isEmpty();
	}

	public boolean containsKey(final K key) {
		return map.containsKey(key);
	}

	public boolean containsValue(final V value) {
		for (final HashSet<V> v : map.values()) {
			if (v.contains(value)) {
				return true;
			}
		}
		return false;
	}

	public boolean containsEntry(final K key, final V value) {
		final HashSet<V> values = map.get(key);
		if (values != null) {
			return values.contains(value);
		} else {
			return false;
		}
	}

	public Collection<V> get(final K key) {
		if (containsKey(key)) {
			return Collections.unmodifiableCollection(map.get(key));
		} else {
			return Collections.emptyList();
		}
	}

	public void put(final K key, final V value) {
		safeGet(key).add(value);
	}

	public Collection<V> removeAll(final K key) {
		return map.remove(key);
	}

	public boolean remove(final K key, final V value) {
		final HashSet<V> values = map.get(key);
		final boolean success = values != null && values.remove(value);

		if (success && values.isEmpty()) {
			map.remove(key);
		}

		return success;
	}

	public void putAll(final MultiHashMap<? extends K, ? extends V> m) {
		for (final Entry<? extends K, ?> e : m.map.entrySet()) {
			final K key = e.getKey();
			@SuppressWarnings("unchecked")
			final HashSet<V> value = (HashSet<V>) e.getValue();
			safeGet(key).addAll(value);
		}
	}

	public void putAll(final K key, final Iterable<V> values) {
		final HashSet<V> value = safeGet(key);
		for (final V v : values) {
			value.add(v);
		}
	}

	private HashSet<V> safeGet(final K key) {
		HashSet<V> values = map.get(key);
		if (values == null) {
			values = new HashSet<>();
			map.put(key, values);
		}
		return values;
	}

	public void clear() {
		map.clear();
	}

	public Set<K> keySet() {
		return map.keySet();
	}

	public Collection<V> values() {
		if (isEmpty()) {
			return Collections.emptyList();
		}

		final ArrayList<V> values = new ArrayList<>();
		for (final Collection<V> v : map.values()) {
			values.addAll(v);
		}
		return values;
	}

	public Collection<Entry<K, V>> entries() {
		final Collection<Entry<K, V>> entries = new HashSet<>();
		for (final Entry<K, HashSet<V>> e : map.entrySet()) {
			for (final V v : e.getValue()) {
				entries.add(new MultiHashMapEntry(e.getKey(), v));
			}
		}
		return entries;
	}

}
