export function routeQueryValue(value: unknown): string {
  if (value == null) return "";
  if (Array.isArray(value)) return routeQueryValue(value[0]);
  return String(value);
}
