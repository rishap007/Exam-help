/**
 * Extract initials from a full name
 * @param name - Full name string
 * @returns Initials (max 2 characters)
 */
export function getInitials(name: string): string {
  if (!name || typeof name !== 'string') return '';
  
  const names = name.trim().split(/\s+/);
  if (names.length === 1) {
    return names[0].charAt(0).toUpperCase();
  }
  
  return (names[0].charAt(0) + names[names.length - 1].charAt(0)).toUpperCase();
}

/**
 * Format a name for display
 * @param firstName - First name
 * @param lastName - Last name  
 * @returns Formatted full name
 */
export function formatName(firstName?: string, lastName?: string): string {
  const first = firstName?.trim() || '';
  const last = lastName?.trim() || '';
  
  if (!first && !last) return 'Anonymous';
  if (!first) return last;
  if (!last) return first;
  
  return `${first} ${last}`;
}

/**
 * Capitalize first letter of each word
 * @param text - Text to capitalize
 * @returns Capitalized text
 */
export function capitalizeWords(text: string): string {
  if (!text) return '';
  
  return text
    .toLowerCase()
    .split(' ')
    .map(word => word.charAt(0).toUpperCase() + word.slice(1))
    .join(' ');
}

/**
 * Truncate text with ellipsis
 * @param text - Text to truncate
 * @param maxLength - Maximum length before truncation
 * @returns Truncated text
 */
export function truncateText(text: string, maxLength: number): string {
  if (!text || text.length <= maxLength) return text;
  
  return text.slice(0, maxLength).trim() + '...';
}

/**
 * Get display name from user object
 * @param user - User object with name properties
 * @returns Display name
 */
export function getDisplayName(user: { 
  firstName?: string; 
  lastName?: string; 
  name?: string; 
  email?: string 
}): string {
  if (user.firstName || user.lastName) {
    return formatName(user.firstName, user.lastName);
  }
  
  if (user.name) {
    return user.name;
  }
  
  if (user.email) {
    return user.email.split('@')[0];
  }
  
  return 'Anonymous';
}
