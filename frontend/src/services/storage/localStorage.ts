/**
 * Local Storage Service
 * Provides type-safe localStorage operations with error handling
 */

const STORAGE_PREFIX = 'eduplatform_';

export const storage = {
  /**
   * Get item from localStorage
   */
  get<T>(key: string, defaultValue?: T): T | null {
    try {
      const item = localStorage.getItem(STORAGE_PREFIX + key);
      return item ? JSON.parse(item) : defaultValue ?? null;
    } catch (error) {
      console.error(`Error reading from localStorage: ${key}`, error);
      return defaultValue ?? null;
    }
  },

  /**
   * Set item in localStorage
   */
  set<T>(key: string, value: T): void {
    try {
      localStorage.setItem(STORAGE_PREFIX + key, JSON.stringify(value));
    } catch (error) {
      console.error(`Error writing to localStorage: ${key}`, error);
    }
  },

  /**
   * Remove item from localStorage
   */
  remove(key: string): void {
    try {
      localStorage.removeItem(STORAGE_PREFIX + key);
    } catch (error) {
      console.error(`Error removing from localStorage: ${key}`, error);
    }
  },

  /**
   * Clear all items from localStorage with prefix
   */
  clear(): void {
    try {
      const keys = Object.keys(localStorage);
      keys.forEach((key) => {
        if (key.startsWith(STORAGE_PREFIX)) {
          localStorage.removeItem(key);
        }
      });
    } catch (error) {
      console.error('Error clearing localStorage', error);
    }
  },

  /**
   * Check if key exists in localStorage
   */
  has(key: string): boolean {
    return localStorage.getItem(STORAGE_PREFIX + key) !== null;
  },

  /**
   * Get all keys from localStorage with prefix
   */
  keys(): string[] {
    try {
      return Object.keys(localStorage)
        .filter((key) => key.startsWith(STORAGE_PREFIX))
        .map((key) => key.replace(STORAGE_PREFIX, ''));
    } catch (error) {
      console.error('Error getting localStorage keys', error);
      return [];
    }
  },
};

/**
 * Specific storage keys for type safety
 */
export const STORAGE_KEYS = {
  ACCESS_TOKEN: 'accessToken',
  REFRESH_TOKEN: 'refreshToken',
  USER: 'user',
  THEME: 'theme',
  SIDEBAR_COLLAPSED: 'sidebarCollapsed',
  RECENT_SEARCHES: 'recentSearches',
  VIDEO_PROGRESS: 'videoProgress',
} as const;