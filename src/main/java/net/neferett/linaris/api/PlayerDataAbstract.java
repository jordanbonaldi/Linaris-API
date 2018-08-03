package net.neferett.linaris.api;

import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public abstract class PlayerDataAbstract {

	protected Date					lastRefresh;
	protected Map<String, String>	playerData	= new ConcurrentHashMap<>();
	protected final String			playerID;

	protected PlayerDataAbstract(final String playerID) {
		this.playerID = playerID;
	}

	/**
	 * Permet de savoir si les données du joueur contiennent une clé en
	 * particulier
	 * 
	 * @param key
	 *            Clé à tester
	 * @return true si cette clé existe
	 */
	public boolean contains(final String key) {
		return this.playerData.containsKey(key);
	}

	/**
	 * Récupère la valeur d'une clé
	 * 
	 * @param key
	 *            clé à récupérer
	 * @return valeur de la clé, null si elle n'existe pas
	 */
	public String get(final String key) {
		return this.playerData.get(key);
	}

	/**
	 * Récupère la valeur d'une clé
	 * 
	 * @param key
	 *            clé à récupérer
	 * @param def
	 *            Valeur par défaut
	 * @return valeur de la clé, <code>def</code> si elle n'existe pas
	 */
	public String get(final String key, final String def) {
		return this.contains(key) ? this.get(key) : def;
	}

	/**
	 * Récupère la valeur d'une clé
	 * 
	 * @param key
	 *            clé à récupérer
	 * @return valeur de la clé, null si elle n'existe pas
	 * @throws net.samagames.api.player.InvalidTypeException
	 *             si la valeur n'est pas du bon type
	 */
	public Boolean getBoolean(final String key) {
		final String val = this.get(key);
		if (val == null)
			return null;

		try {
			return Boolean.valueOf(val);
		} catch (final Exception e) {
			throw new InvalidTypeException("This value is not a boolean.");
		}
	}

	/**
	 * Récupère la valeur d'une clé
	 * 
	 * @param key
	 *            clé à récupérer
	 * @param def
	 *            Valeur par défaut
	 * @return valeur de la clé, <code>def</code> si elle n'existe pas
	 * @throws net.samagames.api.player.InvalidTypeException
	 *             si la valeur n'est pas du bon type
	 */
	public Boolean getBoolean(final String key, final boolean def) {
		final Boolean ret = this.getBoolean(key);
		if (ret == null)
			return def;
		else
			return ret;
	}

	/**
	 * Récupère la valeur d'une clé
	 * 
	 * @param key
	 *            clé à récupérer
	 * @return valeur de la clé, null si elle n'existe pas
	 * @throws net.samagames.api.player.InvalidTypeException
	 *             si la valeur n'est pas du bon type
	 */
	public Double getDouble(final String key) {
		final String val = this.get(key);
		if (val == null)
			return null;

		try {
			return Double.valueOf(val);
		} catch (final Exception e) {
			throw new InvalidTypeException("This value is not a double.");
		}
	}

	/**
	 * Récupère la valeur d'une clé
	 * 
	 * @param key
	 *            clé à récupérer
	 * @param def
	 *            Valeur par défaut
	 * @return valeur de la clé, <code>def</code> si elle n'existe pas
	 * @throws InvalidTypeException
	 *             si la valeur n'est pas du bon type
	 */
	public Double getDouble(final String key, final double def) {
		final Double ret = this.getDouble(key);
		if (ret == null)
			return def;
		else
			return ret;
	}

	/**
	 * Récupère la valeur d'une clé
	 * 
	 * @param key
	 *            clé à récupérer
	 * @return valeur de la clé, null si elle n'existe pas
	 * @throws net.samagames.api.player.InvalidTypeException
	 *             si la valeur n'est pas du bon type
	 */
	public Integer getInt(final String key) {
		final String val = this.get(key);
		if (val == null)
			return 0;

		try {
			return Integer.valueOf(val);
		} catch (final Exception e) {
			throw new InvalidTypeException("This value is not an int.");
		}
	}

	/**
	 * Récupère la valeur d'une clé
	 * 
	 * @param key
	 *            clé à récupérer
	 * @param def
	 *            Valeur par défaut
	 * @return valeur de la clé, <code>def</code> si elle n'existe pas
	 * @throws net.samagames.api.player.InvalidTypeException
	 *             si la valeur n'est pas du bon type
	 */
	public Integer getInt(final String key, final int def) {
		final Integer ret = this.getInt(key);
		if (ret == null)
			return def;
		else
			return ret;
	}

	/**
	 * Obtient les clés des données stockées
	 * 
	 * @return Liste des clés stockées
	 */
	public Set<String> getKeys() {
		return this.playerData.keySet();
	}

	/**
	 * Renvoie la dernière date d'actualisation depuis la base de données
	 * 
	 * @return Dernière actualisation
	 */
	public Date getLastRefresh() {
		return this.lastRefresh;
	}

	/**
	 * Récupère la valeur d'une clé
	 * 
	 * @param key
	 *            clé à récupérer
	 * @return valeur de la clé, null si elle n'existe pas
	 * @throws net.samagames.api.player.InvalidTypeException
	 *             si la valeur n'est pas du bon type
	 */
	public Long getLong(final String key) {
		final String val = this.get(key);
		if (val == null)
			return null;

		try {
			return Long.valueOf(val);
		} catch (final Exception e) {
			throw new InvalidTypeException("This value is not a long.");
		}
	}

	/**
	 * Récupère la valeur d'une clé
	 * 
	 * @param key
	 *            clé à récupérer
	 * @param def
	 *            Valeur par défaut
	 * @return valeur de la clé, <code>def</code> si elle n'existe pas
	 * @throws net.samagames.api.player.InvalidTypeException
	 *             si la valeur n'est pas du bon type
	 */
	public Long getLong(final String key, final long def) {
		final Long ret = this.getLong(key);
		if (ret == null)
			return def;
		else
			return ret;
	}

	/**
	 * Permet d'obtenir l'UUID du joueur
	 * 
	 * @return UUID du joueur
	 */
	public String getPlayerID() {
		return this.playerID;
	}

	/**
	 * Obtient l'ensemble des données du joueur
	 * 
	 * @return données du joueur
	 */
	public Map<String, String> getValues() {
		return this.playerData;
	}

	public abstract void remove(String key);

	/**
	 * Définit une valeur dans les données du joueur
	 * 
	 * @param key
	 *            Clé à définir
	 * @param value
	 *            Valeur à définir
	 */
	public abstract void set(String key, String value);

	/**
	 * Définit une valeur dans les données du joueur
	 * 
	 * @param key
	 *            Clé à définir
	 * @param value
	 *            Valeur à définir
	 */
	public abstract void setBoolean(String key, boolean value);

	/**
	 * Définit une valeur dans les données du joueur
	 * 
	 * @param key
	 *            Clé à définir
	 * @param value
	 *            Valeur à définir
	 */
	public abstract void setDouble(String key, double value);

	/**
	 * Définit une valeur dans les données du joueur
	 * 
	 * @param key
	 *            Clé à définir
	 * @param value
	 *            Valeur à définir
	 */
	public abstract void setInt(String key, int value);

	/**
	 * Définit une valeur dans les données du joueur
	 * 
	 * @param key
	 *            Clé à définir
	 * @param value
	 *            Valeur à définir
	 */
	public abstract void setLong(String key, long value);
}
