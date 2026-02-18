export const formatEnum = (value) => {
  if (!value) return '';
  return value
    .toString()
    .split('_')
    .map((word) => word.charAt(0) + word.slice(1).toLowerCase())
    .join(' ');
};

export const factionTone = (faction) => {
  if (faction === 'ALIANZA') return 'alliance';
  if (faction === 'HORDA') return 'horde';
  return '';
};
