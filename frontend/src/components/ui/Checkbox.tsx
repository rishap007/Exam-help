import * as React from 'react';
import { Check } from 'lucide-react';
import { cn } from '@/utils/cn';

export interface CheckboxProps
  extends Omit<React.InputHTMLAttributes<HTMLInputElement>, 'type'> {
  label?: string;
  error?: string;
}

const Checkbox = React.forwardRef<HTMLInputElement, CheckboxProps>(
  ({ className, label, error, id, ...props }, ref) => {
    const checkboxId = id || label?.toLowerCase().replace(/\s+/g, '-');

    return (
      <div className="space-y-2">
        <div className="flex items-center gap-2">
          <div className="relative">
            <input
              id={checkboxId}
              type="checkbox"
              className="peer sr-only"
              ref={ref}
              {...props}
            />
            <div
              className={cn(
                'flex h-5 w-5 items-center justify-center rounded border-2 border-input ring-offset-background transition-colors',
                'peer-focus-visible:ring-2 peer-focus-visible:ring-ring peer-focus-visible:ring-offset-2',
                'peer-checked:border-primary peer-checked:bg-primary peer-checked:text-primary-foreground',
                'peer-disabled:cursor-not-allowed peer-disabled:opacity-50',
                error && 'border-destructive',
                className
              )}
            >
              <Check className="h-3.5 w-3.5 opacity-0 transition-opacity peer-checked:opacity-100" />
            </div>
          </div>
          {label && (
            <label
              htmlFor={checkboxId}
              className="text-sm font-medium leading-none peer-disabled:cursor-not-allowed peer-disabled:opacity-70"
            >
              {label}
            </label>
          )}
        </div>
        {error && <p className="text-sm text-destructive">{error}</p>}
      </div>
    );
  }
);
Checkbox.displayName = 'Checkbox';

export { Checkbox };