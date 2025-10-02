import { create } from 'zustand';
import { persist } from 'zustand/middleware';

interface UIState {
  theme: 'light' | 'dark' | 'system';
  sidebarOpen: boolean;
  sidebarCollapsed: boolean;
  notificationsPanelOpen: boolean;
  searchOpen: boolean;
}

interface UIActions {
  setTheme: (theme: 'light' | 'dark' | 'system') => void;
  toggleTheme: () => void;
  setSidebarOpen: (open: boolean) => void;
  toggleSidebar: () => void;
  setSidebarCollapsed: (collapsed: boolean) => void;
  toggleSidebarCollapsed: () => void;
  setNotificationsPanelOpen: (open: boolean) => void;
  toggleNotificationsPanel: () => void;
  setSearchOpen: (open: boolean) => void;
  toggleSearch: () => void;
}

type UIStore = UIState & UIActions;

const initialState: UIState = {
  theme: 'system',
  sidebarOpen: true,
  sidebarCollapsed: false,
  notificationsPanelOpen: false,
  searchOpen: false,
};

export const useUIStore = create<UIStore>()(
  persist(
    (set) => ({
      ...initialState,

      setTheme: (theme) => {
        // Apply theme to document
        const root = document.documentElement;
        
        if (theme === 'system') {
          const prefersDark = window.matchMedia('(prefers-color-scheme: dark)').matches;
          root.classList.toggle('dark', prefersDark);
        } else {
          root.classList.toggle('dark', theme === 'dark');
        }

        set({ theme });
      },

      toggleTheme: () =>
        set((state) => {
          const themes: Array<'light' | 'dark' | 'system'> = ['light', 'dark', 'system'];
          const currentIndex = themes.indexOf(state.theme);
          const nextTheme = themes[(currentIndex + 1) % themes.length];

          // Apply theme to document
          const root = document.documentElement;
          
          if (nextTheme === 'system') {
            const prefersDark = window.matchMedia('(prefers-color-scheme: dark)').matches;
            root.classList.toggle('dark', prefersDark);
          } else {
            root.classList.toggle('dark', nextTheme === 'dark');
          }

          return { theme: nextTheme };
        }),

      setSidebarOpen: (open) => set({ sidebarOpen: open }),
      
      toggleSidebar: () => set((state) => ({ sidebarOpen: !state.sidebarOpen })),

      setSidebarCollapsed: (collapsed) => set({ sidebarCollapsed: collapsed }),
      
      toggleSidebarCollapsed: () =>
        set((state) => ({ sidebarCollapsed: !state.sidebarCollapsed })),

      setNotificationsPanelOpen: (open) => set({ notificationsPanelOpen: open }),
      
      toggleNotificationsPanel: () =>
        set((state) => ({ notificationsPanelOpen: !state.notificationsPanelOpen })),

      setSearchOpen: (open) => set({ searchOpen: open }),
      
      toggleSearch: () => set((state) => ({ searchOpen: !state.searchOpen })),
    }),
    {
      name: 'ui-storage',
      partialize: (state) => ({
        theme: state.theme,
        sidebarCollapsed: state.sidebarCollapsed,
      }),
    }
  )
);

// Selectors
export const selectTheme = (state: UIStore) => state.theme;
export const selectSidebarOpen = (state: UIStore) => state.sidebarOpen;
export const selectSidebarCollapsed = (state: UIStore) => state.sidebarCollapsed;