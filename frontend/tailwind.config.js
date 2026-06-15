/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./src/**/*.{html,ts}",
  ],
  theme: {
    extend: {
      colors: {
        'bg-primary': '#0A1628',
        'bg-sidebar': '#0D1B2A',
        'bg-card': '#1B2838',
        'accent': '#00E676',
        'moss': '#4A7C59',
        'forest': '#2D5A3D',
        'earth': '#3D2B1F',
        'danger': '#FF5252',
        'warning': '#FFC107',
        'info': '#4FC3F7',
        'text-primary': '#E6EDF3',
        'text-secondary': '#8B949E',
        'border': '#30363D',
        'border-hover': '#3A4A5A',
      },
      fontFamily: {
        'inter': ['Inter', 'sans-serif'],
      },
    },
  },
  plugins: [],
}
