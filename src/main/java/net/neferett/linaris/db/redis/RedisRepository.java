package net.neferett.linaris.db.redis;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import redis.clients.jedis.Jedis;

public class RedisRepository<T extends RedisEntity> {

	LinkedHashMap<String, T>	cache;
	RedisDatabase				database;

	String						repositoryName;

	public RedisRepository(final String name, final RedisDatabase database) {
		this.repositoryName = name;
		this.database = database;
	}

	public void deleteEntity(final String id) {
		try {
			final Jedis jedis = this.database.getResource();
			jedis.del(this.repositoryName + ":entries:" + id);
			jedis.close();
			this.cache.remove(id);
		} catch (final Exception e) {
			System.out.println("[Repository] Cannot connect to redis server : " + e.getMessage());
		}
	}

	public void deleteEntity(final T Entity) {
		if (!Entity.haveId())
			return;
		this.deleteEntity(Entity.getId());
	}

	public Collection<T> entities() {
		return this.entities(false);
	}

	@SuppressWarnings("unchecked")
	public Collection<T> entities(final boolean forceUpdate) {
		if (!forceUpdate && this.cache != null)
			return new ArrayList<>(this.cache.values());

		final Collection<T> entities = new ArrayList<>();
		try {
			final Jedis jedis = this.database.getResource();
			final Set<String> keys = jedis.keys(this.repositoryName + ":entries:*");
			for (final String key : keys) {
				final String value = jedis.get(key);
				final RedisEntity entity = this.getClassOfEntity().newInstance().fromJson(value);
				entities.add((T) entity);
			}
			jedis.close();
			this.cache = new LinkedHashMap<>(
					entities.stream().collect(Collectors.toMap(T::getId, Function.identity())));
		} catch (final Exception e) {
			e.printStackTrace();
			System.out.println("[Repository] Cannot connect to redis server : " + e.getMessage());
		}
		return entities;
	}

	public Collection<T> findAll(final Predicate<T> predicate, final boolean forceUpdate) {
		return this.entities(forceUpdate).stream().filter(predicate).collect(Collectors.toList());
	}

	@SuppressWarnings("unchecked")
	public Class<T> getClassOfEntity() {
		return (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}

	public T getEntityById(final String id) {
		return this.getEntityById(id, false);
	}

	@SuppressWarnings("unchecked")
	public T getEntityById(final String id, final boolean forceUpdate) {
		if (!forceUpdate)
			return this.cache.get(id);
		try {
			final Jedis jedis = this.database.getResource();
			final String value = jedis.get(this.repositoryName + ":entries:" + id);
			final RedisEntity entity = this.getClassOfEntity().newInstance().fromJson(value);
			return (T) entity;
		} catch (final Exception e) {
			System.out.println("[Repository] Cannot connect to redis server : " + e.getMessage());
			return null;
		}
	}

	public String getRepositoryName() {
		return this.repositoryName;
	}

	public T saveEntity(final T entity) {
		if (!entity.haveId())
			entity.setId(UUID.randomUUID().toString());
		try {
			final Jedis jedis = this.database.getResource();
			jedis.set(this.repositoryName + ":entries:" + entity.getId(), entity.toJson());
			jedis.close();
			this.cache.put(entity.getId(), entity);

		} catch (final Exception e) {
			System.out.println("[Repository] Cannot connect to redis server : " + e.getMessage());
		}
		return entity;
	}

}
